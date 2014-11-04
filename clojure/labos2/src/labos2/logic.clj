(ns labos2.logic
  (:use [clojure.core.match :only [match]] :verbose))

;;;; logic helpers
(defn op [expr] (if (symbol? expr) expr (first expr)))
(defn args [expr] (rest expr))
(defn arg1 [expr] (first (rest expr)))
(defn arg2 [expr] (second (rest expr)))

(def +logical-connectives+ #{:and :or :not '-> '<-> '| '&})

(defn atomic-clause? [sentence]
  "An atomic clause has no connectives or quantifiers."
  (not (contains? +logical-connectives+ (op sentence))))

(defn negative-clause? [sentence]
  "A negative clause has NOT as the operator."
  (= (op sentence) :not))

(defn literal-clause? [sentence]
  "A literal is an atomic clause or a negated atomic clause."
  (or (atomic-clause? sentence)
      (and (negative-clause? sentence) (atomic-clause? (arg1 sentence)))))

(defn conjuncts [sentence]
  "Return a list of the conjuncts in this sentence."
  (cond (= (op sentence) :and) (args sentence)
	(= sentence :true) nil
	:else (list sentence)))

(defn disjuncts [sentence]
  "Return a list of the disjuncts in this sentence."
  (cond (= (op sentence) :or) (args sentence)
	(= sentence :false) nil
	:else (list sentence)))

(defn conjunction [args]
  "Form a conjunction with these args."
  (match [(count args)]
         [0] :true
         [1] (first args)
         [_] (cons :and args)))

(defn disjunction [args]
  "Form a disjunction with these args."
  (match [(count args)]
         [0] :false
         [1] (first args)
         [_] (cons :or args)))

(defn move-not-inwards [expr]
  "Given P, return ~P, but with the negation moved as far in as possible."
  (match [(op expr)]
         [:not] (arg1 expr)
         [:and] (disjunction (map move-not-inwards (args expr)))
         [:or]  (conjunction (map move-not-inwards (args expr)))
         [_] `(:not ~expr)))

(defn f [x y] (disjunction (concat (disjuncts x) (disjuncts y))))
(defn g [y expr] (map #(f % y) (conjuncts (first expr))))
(defn distributive-law [expr]
  "Applies distributive law: 'F | (G & H) = (F | G) & (F | H)'."
  (match [(count expr)]
              [0] :false
              [1] (first expr)
              [_] (conjunction (mapcat #(g % expr) (conjuncts (distributive-law (rest expr)))))))

(defn negate [sentence]
  "Negates the given sentence."
  (move-not-inwards sentence))
