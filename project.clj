(defproject primitive-math "0.1.5"
  :description "predictable, primitive math"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies []
  :profiles {:dev {:dependencies [[criterium "0.4.1"]
                                  [org.clojure/clojure "1.8.0"]
                                  [codox-md "0.2.0" :exclusions [org.clojure/clojure]]]}}
  :plugins [[codox "0.6.4"]]
  :codox {:writer codox-md.writer/write-docs
          :include [primitive-math]}
  :global-vars {*warn-on-reflection* true}
  :jvm-opts ^:replace ["-server"]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.5" "-source" "1.5"])
