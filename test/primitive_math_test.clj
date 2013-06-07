(ns primitive-math-test
  (:use
    clojure.test)
  (:require
    [primitive-math :as p]))

(defn eval-assertions [x]
  (eval
    `(do
       ~@(map #(list `is %) x))))

(deftest test-arithmetic
  (p/use-primitive-operators)
  (try
    (eval-assertions
      '((== 6 (+ 1 2 3) (+ 3 3) (+ 6))
        (== 0 (- 6 3 3) (- 6 6))
        (== -3 (- 3))
        (== 12 (* 2 2 3) (* 4 3))
        (== 5 (/ 10 2) (/ 20 2 2) (/ 11 2))

        (= true (and true) (and true true) (or true) (or false true))
        (= false (and false) (or false) (and true false))

        (== 6.0 (+ 1.0 2.0 3.0) (+ 3.0 3.0))

        (thrown? IllegalArgumentException
          (+ 1 2.0))))
    (finally
      (p/unuse-primitive-operators)))
  
  (eval-assertions
    `((== 6 (+ 1 2 3) (+ 3 3))
      (== 3.0 (+ 1 2.0)))))


