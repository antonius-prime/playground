(ns labos2.ast
  (:use [clojure.core.match :only [match]])
  (:require clojure.walk))


;;;; ast
(defn unary-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[x A]]
  (list x A))

(defn order-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[A x B & more]]
  (let [ret (list x A B)]
    (if more
      (recur (concat ret more))
      ret)))

(defn one-neg-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[a b c d]]
  (match [a b c d]
         ['! _ op _] (list op (list a b) d)
         [_ op '! _] (list op a (list c d))))

(defn two-neg-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[a b c d e]]
  (match [a b c d e]
         ['! _ op '! _] (list op (list a b) (list d e))
         [_ _ _ _ _] (throw (Throwable. "Error."))))


(defn add-parens
  "Tree walk to add parens.  All lists are length 3 afterwards."
  [s]
  (clojure.walk/postwalk
   #(if (seq? %)
      (let [c (count %) ]
	(cond (= c 1) (first %)
              (= c 2) (unary-ops %)
	      (= c 3) (order-ops %)
              (= c 4) (one-neg-ops %)
	      (= c 5) (two-neg-ops %))) ;todo
      %)
   s))

(def connectives-to-keywords
  "Replace with more connectives symbols with more-convenient keywords."
  (partial clojure.walk/postwalk-replace {'| :or '! :not '& :and}))

(defn make-ast
  "Parse a string into a list of numbers, ops, and lists"
  [s]
  (-> (format "'(%s)" s)
      (.replaceAll , "~" "!")
      (.replaceAll , "([&|!A-Z])" " $1 ")
      load-string
      add-parens
      connectives-to-keywords))
