(ns labos2.core
  (:gen-class :main true)
  (:use [labos2.refutation :only [start-resolution]] :verbose))

(defn get-input [prompt]
  (println prompt)
  (read-line))

;; lein run args
(defn -main
  "Input point of the program, takes two arguments. A formula and a mode [0|1]."
  [& args]
  (let [premises (get-input "Provide premises formula")
        goal     (get-input "Provide goal formula")
        strategy (get-input "Provide proof strategy (0 - set of support, 1 - set of support plus simplification).")
        s (Integer/parseInt strategy)]
    (if (contains? [0 1] s)
      (start-resolution premises goal s)
      (recur (println "Bad args given.")))))
