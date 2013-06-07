(ns primitive-math
  (:refer-clojure
    :exclude [* + - / < > <= >= == rem bit-or bit-and bit-shift-left bit-shift-right byte short int float inc dec zero?])
  (:import
    [primitive_math Primitives]))

;;;

(defmacro ^:private variadic-proxy
  "Creates left-associative variadic forms for any operator."
  ([name fn]
     `(variadic-proxy ~name ~fn identity))
  ([name fn single-arg-form]
     (let [x-sym (gensym "x")]
       `(defmacro ~name
          ([~x-sym]
             ~((eval single-arg-form) x-sym))
          ([x# y#]
             (list '~fn x# y#))
          ([x# y# ~'& rest#]
             (list* '~name (list '~name x# y#) rest#))))))

(defmacro ^:private variadic-predicate-proxy
  "Turns variadic predicates into multiple pair-wise comparisons."
  ([name fn]
     `(variadic-predicate-proxy ~name ~fn (constantly true)))
  ([name fn single-arg-form]
     (let [x-sym (gensym "x")]
       `(defmacro ~name
          ([~x-sym]
             ~((eval single-arg-form) x-sym))
          ([x# y#]
             (list '~fn x# y#))
          ([x# y# ~'& rest#]
             (list 'primitive_math.Primitives/and (list '~name x# y#) (list* '~name y# rest#)))))))

(variadic-proxy +             primitive_math.Primitives/add)
(variadic-proxy -             primitive_math.Primitives/subtract (fn [x] `(list 'primitive_math.Primitives/negate ~x)))
(variadic-proxy *             primitive_math.Primitives/multiply)
(variadic-proxy /             primitive_math.Primitives/divide)
(variadic-proxy div           primitive_math.Primitives/divide)
(variadic-proxy bit-and       primitive_math.Primitives/bitAnd)
(variadic-proxy bit-or        primitive_math.Primitives/bitOr)
(variadic-proxy bool-and      primitive_math.Primitives/and)
(variadic-proxy bool-or       primitive_math.Primitives/or)

(variadic-predicate-proxy >   primitive_math.Primitives/gt)
(variadic-predicate-proxy <   primitive_math.Primitives/lt)
(variadic-predicate-proxy <=  primitive_math.Primitives/lte)
(variadic-predicate-proxy >=  primitive_math.Primitives/gte)
(variadic-predicate-proxy ==  primitive_math.Primitives/eq)
(variadic-predicate-proxy not==  primitive_math.Primitives/neq)

(defmacro inc [x]
  `(Primitives/inc ~x))

(defmacro dec [x]
  `(Primitives/dec ~x))

(defmacro rem [n div]
  `(Primitives/rem ~n ~div))

(defmacro zero? [x]
  `(Primitives/isZero ~x))

(defmacro bool-not [x]
  `(Primitives/not ~x))

(defmacro bit-shift-left [n bits]
  `(Primitives/shiftLeft ~n ~bits))

(defmacro bit-shift-right [n bits]
  `(Primitives/shiftRight ~n ~bits))

(defmacro bit-unsigned-shift-right [n bits]
  `(Primitives/unsignedShiftRight ~n ~bits))

(defmacro << [n bits]
  `(Primitives/shiftLeft ~n ~bits))

(defmacro >> [n bits]
  `(Primitives/shiftRight ~n ~bits))

(defmacro >>> [n bits]
  `(Primitives/unsignedShiftRight ~n ~bits))

;;;

(def ^:private vars-to-exclude
  '[* + - / < > <= >= == rem bit-or bit-and bit-shift-left bit-shift-right byte short int float inc dec zero?])

(defn- using-primitive-operators? []
  (= #'primitive-math/+ (resolve '+)))

(defonce ^:private hijacked? (atom false))

(defn- ns-wrapper
  "Makes sure that if a namespace that is using primitive operators is reloaded, it will automatically
   exclude the shadowed operators in `clojure.core`."
  [f]
  (fn [& x]
    (if-not (using-primitive-operators?)
      (apply f x)
      (let [refer-clojure (->> x
                            (filter #(and (sequential? %) (= :refer-clojure (first %))))
                            first)
            refer-clojure-clauses (update-in
                                    (apply hash-map (rest refer-clojure))
                                    [:exclude]
                                    #(concat % vars-to-exclude))]
        (apply f
          (concat
            (remove #{refer-clojure} x)
            [(list* :refer-clojure (apply concat refer-clojure-clauses))]))))))

(defn use-primitive-operators
  "Replaces Clojure's arithmetic and number coercion functions with primitive equivalents.  These are
   defined as macros, so they cannot be used as higher-order functions.  This is an idempotent operation.."
  []
  (when-not @hijacked?
    (reset! hijacked? true)
    (alter-var-root #'clojure.core/ns ns-wrapper))
  (when-not (using-primitive-operators?)
    (doseq [v vars-to-exclude]
      (ns-unmap *ns* v))
    (require (vector 'primitive-math :refer vars-to-exclude))))

(defn unuse-primitive-operators
  "Undoes the work of `use-primitive-operators`.  This is idempotent."
  []
  (doseq [v vars-to-exclude]
    (ns-unmap *ns* v))
  (refer 'clojure.core))

;;;

(defn byte 
  "Truncates a number to a byte, will not check for overflow."
  {:inline (fn [x] `(primitive_math.Primitives/toByte ~x))}
  ^long [^long x]
  (long (Primitives/toByte x)))

(defn short
  "Truncates a number to a short, will not check for overflow."
  {:inline (fn [x] `(primitive_math.Primitives/toShort ~x))}
  ^long [^long x]
  (long (Primitives/toShort x)))

(defn int
  "Truncates a number to an int, will not check for overflow."
  {:inline (fn [x] `(primitive_math.Primitives/toInteger ~x))}
  ^long [^long x]
  (long (Primitives/toInteger x)))

(defn float
  "Truncates a number to a float, will not check for overflow."
    {:inline (fn [x] `(primitive_math.Primitives/toFloat ~x))}
  ^double [^double x]
  (double (Primitives/toFloat x)))

