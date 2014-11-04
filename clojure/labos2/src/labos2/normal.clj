(ns labos2.normal
  (:use [labos2.ast]
        [labos2.logic]
        [clojure.core.match :only [match]]))

(defn eliminate-implication [expr]
  "Eliminate implication occurrences by using rule: P -> R = ~P | R."
  (if (literal-clause? expr) expr
      (match [(op expr)]
               ['<->] `(:and (:or (:not ~(arg1 expr)) ~(arg2 expr))
                             (:or ~(arg1 expr) (:not ~(arg2 expr)) ))
               ['->] `(:or (:not ~(arg1 expr)) ~(arg2 expr))
               [_] (cons (op expr) (map eliminate-implication (args expr))))))

(defn ->cnf [expr]
  "A step in turning the sentecne to CNF."
  (let [expr (eliminate-implication expr)]
    (match [(op expr)]
           [:not] (let [next-expr (move-not-inwards (arg1 expr))]
                    (if (literal-clause? next-expr) next-expr (->cnf next-expr)))
           [:and] (let [bla (mapcat #(conjuncts (->cnf %)) (args expr))]
                     (conjunction bla))
           [:or] (if (= (arg1 expr) (arg2 expr))
                   (do)
                   (distributive-law (map ->cnf (args expr))))
           [_] expr)))

(defn cnfConvert [expr]
  (let [expr (make-ast expr)]
    (loop [old-expr expr next-expr (->cnf expr)]
      (if (= old-expr next-expr) old-expr (recur next-expr (->cnf next-expr))))))
