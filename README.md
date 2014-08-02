Clojure's numeric tower is useful, but it can put a lot of steps between you and simple arithmetic.  Unfortunately, while Clojure will warn you when reflection is required to invoke a function, it will **not** warn you when reflection is required to perform math.  The only reliable way to discover whether you're calling `clojure.lang.Number.add(Object, Object)` or `clojure.lang.Number.add(long, long)` is to use a profiler or decompiler.

Or you can just bypass Clojure's math operators altogether.

In the `primitive-math` namespace, there are equivalents for every arithmetic operator and comparator that will give a reflection warning if it cannot compile down to a simple, predictable, unboxed mathematical operation.

```clj
primitive-math> (set! *warn-on-reflection* true)
true
primitive-math> (+ 3 3)
6
primitive-math> (defn adder [x] (+ 1 x))
;; gives a reflection warning
primitive-math> (defn adder [^long x] (+ 1 x))
;; no reflection warning
primitive-math> (+ 3.0 3)
;; gives a reflection warning AND throws an exception
```

To support operations on both `long` and `double` types without any reflection, these operators are defined as macros.  This means they cannot be used as higher-order functions:

```clj
primitive-math> (apply + [1 2 3])
;; throws a 'cannot take value of macro' exception
```

In practice, it's usually preferable to import the namespace with a prefix, and use `p/+` and `p/==` operators alongside the normal Clojure functions.  However, if in a particular namespace you never need to use higher-order operators, you can call `(primitive-math/use-primitive-operators)` to swap out the Clojure operators for their primitive equivalents.  This can be reversed, using `(primitive-math/unuse-primitive-operators)`.

### usage

```clj
[primitive-math "0.1.3"]
```

### an exhaustive list of operators

```clj
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
bit-unsigned-shift-right  ;; aliased as '>>>'
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

Full documentation can be found [here](http://ideolalia.com/primitive-math/primitive-math.html).

### license

Copyright © 2013 Zachary Tellman

Distributed under the [MIT License](http://opensource.org/licenses/MIT).
