(ns clj-commons.primitive-math-test
  (:use
    clojure.test)
  (:require
    [clj-commons.primitive-math :as p]))

(set! *warn-on-reflection* true)

(def primitive-ops
  {:long    `[p/long->ulong     p/ulong->long    p/reverse-long]
   :int     `[p/int->uint       p/uint->int      p/reverse-int]
   :short   `[p/short->ushort   p/ushort->short  p/reverse-short]
   :byte    `[p/byte->ubyte     p/ubyte->byte    identity]
   :float  ` [identity          identity         p/reverse-float]
   :double  `[identity          identity         p/reverse-double]})

(deftest test-roundtrips
  (are [type nums]
    (let [[s->u u->s reverse-fn] (primitive-ops type)]
      (let [s->u' (eval s->u)
            u->s' (eval u->s)
            reverse-fn' (eval reverse-fn)]
        (every?
          (fn [x]
            (and
             ;; test both normal and inlined versions of the functions
              (= x (eval `(-> ~x ~s->u ~u->s)))
              (= x (-> x s->u' u->s'))
              (= x (eval `(-> ~x ~reverse-fn ~reverse-fn)))
              (= x (-> x reverse-fn' reverse-fn'))))
          nums)))

    :double [-1.0 0.0 1.0 Double/MIN_VALUE   Double/MAX_VALUE]
    :float  [-1.0 0.0 1.0 Float/MIN_VALUE    Float/MAX_VALUE]
    :long   [-1 0 1       Long/MIN_VALUE     Long/MAX_VALUE]
    :int    [-1 0 1       Integer/MIN_VALUE  Integer/MAX_VALUE]
    :short  [-1 0 1       Short/MIN_VALUE    Short/MAX_VALUE]
    :byte   [-1 0 1       Byte/MIN_VALUE     Byte/MAX_VALUE]))

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

        (== (float 6.0) (+ (float 1.0) (float 2.0) (float 3.0)) (+ (float 3.0) (float 3.0)))

        (thrown? IllegalArgumentException
          (+ 1 2.0))))
    (finally
      (p/unuse-primitive-operators)))

  (eval-assertions
    `((== 6 (+ 1 2 3) (+ 3 3))
      (== 3.0 (+ 1 2.0)))))
