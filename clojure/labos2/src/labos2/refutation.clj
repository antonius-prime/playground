(ns labos2.refutation
  (:use [labos2.normal]
        [labos2.logic] :verbose
        [clojure.set :as set]))

;;;; Utility functions.
(defn set-size [s1 s2]
  "Compare two sets by the number of elements."
  (let [c (compare (count s1) (count s2))]
    (if (not= 0 c) c 1)))

(defn set-size-vec [[x1 y1][x2 y2]]
  "Compare pair of sets by the number of elements in each set."
  (let [c (+ (compare (count x1) (count x2)) (compare (count y1) (count y2)))]
    (if (not= 0 c) c 1)))

(defn negate-formula [formula]
  "Negates the given sentence."
  (format "~(%s)" formula))

(defn ->minimal-cnf
  "Convert a logical sentence to minimal CNF (no and/or connectives)."
  ([sentence]
     (->minimal-cnf sentence identity))
  ([sentence f]
     (into #{}
           (map #(into #{} (disjuncts %)) (conjuncts (f (cnfConvert sentence)))))))

;;;; Simplification strategy functions.
(defn tautology? [clause]
  "Is clause a tautology (something that is always true)?"
  (some (fn [literal]
	    (and (= (op literal) :not)
                 (contains? clause (arg1 literal))))
	clause))

(defn remove-redundant [clauses]
  "Given set of clauses, remove redundants."
  (reduce (fn [acc c] (if-not (some #(set/subset? % c) (disj clauses c)) (conj acc c ) acc)) #{} clauses))

(defn remove-irrelevent [clauses]
  "Given set of clauses, remove irrelevents."
  (into #{} (filter (comp not tautology?) clauses))  )

(defn simplify [clauses]
  (-> clauses
      remove-irrelevent
      remove-redundant))

;;;; Refutation resolution functions.

(defn plResolve [c1 c2] ;; one from sos
  "Resolves two clauses and returns a set of resolvents."
  (let [neg-c1 (into #{} (map negate c1))
        neg-c2 (into #{} (map negate c2))]
    (set/union (set/difference c1 neg-c2) (set/difference c2 neg-c1))))

(defn pps [s]
  "Turn set of clauses into more readable printing format."
  (map #(if (= (op %) :not) (str "~" (arg1 %)) (str %)) s))

(defn add-to-proof [proof step]
  (if (some #(= (:resolved %) (:resolved step)) proof)
    proof
    (conj proof step)))

(defn check-clauses [kb proof]
  (loop [new #{}  clauses (into (sorted-set-by set-size-vec) kb) steps-count 0 proof proof]
    (if (empty? clauses)
      [new steps-count proof]
      (let [[c1 c2] (first clauses)
            resolvents (plResolve c1 c2)
            ext-proof (add-to-proof proof {:resolved (pps resolvents) :by [(pps c1) (pps c2)] :no (count proof)})]
        (if (empty? resolvents)
          [true steps-count ext-proof]
          (recur (conj new resolvents)
                 (rest clauses)
                 (inc steps-count)
                 ext-proof))))))

(defn selectClauses
  "Selects a set of clause pairs to resolve."
  ([sos kb]
     (for [c1 (into #{} sos)
           c2 (into #{} (disj (set/union sos kb) c1))
           :let [neg-c1 (into #{} (map negate c1))]
           :when (seq (set/intersection neg-c1 c2) )]
       [c1 c2])))

(defn init-proof [premises]
  (into #{} (reduce (fn [proof r] (conj proof {:resolved (pps r) :no (count proof)})) [] premises)))

(defn plResolution [premises goal strategy]
  "Propositional logic theorem prover based on refutation resolution."
  (loop [kb (strategy (->minimal-cnf premises))
         SOS (strategy (->minimal-cnf (negate-formula goal)))
         clauses (set/union SOS kb)
         steps-sum 0
         max-clauses (count clauses)
         proof-steps (init-proof (concat kb SOS))]
    (let [[new steps-count proof] (check-clauses (selectClauses  SOS kb) proof-steps) ]
      (cond (true? new)               {:result "Proven."
                                       :steps (+ steps-count steps-sum)
                                       :clauses max-clauses
                                       :proof (into proof-steps proof)}
            (set/subset? new clauses) {:result "Not proven."
                                       :steps (+ steps-count steps-sum)
                                       :clauses max-clauses}
            :else (recur kb
                         (strategy (set/union SOS new))
                         (strategy  (set/union kb SOS new))
                         (+ steps-count steps-sum)
                         (max max-clauses (count clauses))
                         (into proof-steps proof))))))

(defn start-resolution [premises goal strategy-index]
  "Starts the plResolution using a given strategy."
  (let [strategy (cond (= strategy-index 0) identity
                       (= strategy-index 1) simplify)
        resolution (plResolution premises goal strategy)
        proof (into (sorted-set-by (fn [fst snd] (< (:no fst) (:no snd)))) (:proof  resolution))
        fprf (partial  clojure.string/join " v ")
        printing-proof (fn [p] (if (empty? (:by p))
                                (println (clojure.string/join " v " (:resolved p)))
                                (if (empty? (:resolved p))
                                  (println 'NIL "from" (fprf (first (:by p))) "and" (fprf (second (:by p))))
                                  (println  (fprf (:resolved p)) "from" (fprf (first (:by p))) "and" (fprf (second (:by p)))))))]
    (do
      (println (:result resolution))
      (println "Number of steps:" (:steps resolution))
      (println "Maximum number of clauses:" (:clauses resolution))
      (dorun (map printing-proof proof)))))

;
;(start-resolution "(A&B)<->C" "C->B" 1)


;(start-resolution "C&(C->B)" "B" 1)

;(plResolution "((~P &Q) <-> (R|S)) | (~P->S)" , "((~S&R) -> (Q&P)) | ((P &R) | Q)" )
;(def res (start-resolution "((~P &Q) <-> (R|S)) | (~P->S)" , "((~S&R) -> (Q&P)) | ((P &R) | Q)" 1))

;(->minimal-cnf  "((~P &Q) <-> (R|S)) | (~P->S)")
;(cnfConvert "C&(C->B)")
