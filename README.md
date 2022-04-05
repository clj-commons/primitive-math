[![Clojars Project](https://img.shields.io/clojars/v/org.clj-commons/primitive-math.svg)](https://clojars.org/org.clj-commons/primitive-math)
[![cljdoc badge](https://cljdoc.org/badge/org.clj-commons/primitive-math)](https://cljdoc.org/d/org.clj-commons/primitive-math)
[![CircleCI](https://circleci.com/gh/clj-commons/primitive-math.svg?style=svg)](https://circleci.com/gh/clj-commons/primitive-math)

### About

Clojure's numeric tower is useful, but it can put a lot of steps between you and simple arithmetic.  Unfortunately, while Clojure will warn you when reflection is required to invoke a function, it will **not** warn you when reflection is required to perform math.  The only reliable way to discover whether you're calling `clojure.lang.Number.add(Object, Object)` or `clojure.lang.Number.add(long, long)` is to use a profiler or decompiler.

Or, you can just bypass Clojure's math operators altogether.

In the `clj-commons.primitive-math` namespace, there are equivalents for every arithmetic operator and comparator that will give a reflection warning if it cannot compile down to a simple, predictable, unboxed mathematical operation.

```clojure
clj-commons.primitive-math> (set! *warn-on-reflection* true)
true
clj-commons.primitive-math> (+ 3 3)
6
clj-commons.primitive-math> (defn adder [x] (+ 1 x))
;; gives a reflection warning
clj-commons.primitive-math> (defn adder [^long x] (+ 1 x))
;; no reflection warning
clj-commons.primitive-math> (+ 3.0 3)
;; gives a reflection warning AND throws an exception
```

To support operations on both `long` and `double` types without any reflection, these operators are defined as macros.  This means they cannot be used as higher-order functions:

```clojure
clj-commons.primitive-math> (apply + [1 2 3])
;; throws a 'cannot take value of macro' exception
```

In practice, it's usually preferable to import the namespace with a prefix, and use `p/+` and `p/==` operators alongside the normal Clojure functions.  However, if in a particular namespace you never need to use higher-order operators, you can call `(primitive-math/use-primitive-operators)` to swap out the Clojure operators for their primitive equivalents.  This can be reversed, using `(primitive-math/unuse-primitive-operators)`.

##### Notes

Pre-1.0.0 versions of primitive-math used a single-segment namespace. This causes problems for Graal and clj-easy. For 1.0.0, everything was copied under the `clj-commons` namespace. The code is effectively identical, however, so unless you are using Graal, you don't need to make any changes. If you *are* using Graal, make sure you only require the `clj-commons.*` namespaces to avoid issues.

### Usage

```clojure
;;; Lein
[clj-commons/primitive-math "1.0.0"]

;;; deps.edn
primitive-math/primitive-math {:mvn/version "1.0.0"}
```

### An exhaustive list of operators

```clojure
+
-
*
/  ;; aliased as 'div'
inc
dec
rem
==
not==
zero?
<=
>=
<
>
min
max
bool-and
bool-or
bool-not
bool-xor
true?
false?
bit-and
bit-or
bit-xor
bit-not
bit-shift-left   ;; aliased as '<<'
bit-shift-right  ;; aliased as '>>'
unsigned-bit-shift-right  ;; aliased as '>>>'
byte
short
int
float
long
double
byte->ubyte
ubyte->byte
short->ushort
ushort->short
int->uint
uint->int
long->ulong
ulong->long
reverse-short
reverse-int
reverse-long
```

Full documentation can be found at [cljdoc](https://cljdoc.org/d/org.clj-commons/primitive-math).

### License

Copyright © 2016 Zachary Tellman

Distributed under the [MIT License](http://opensource.org/licenses/MIT).
