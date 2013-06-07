(defproject primitive-math "0.1.1"
  :description "predictable, primitive math"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies []
  :profiles {:dev {:dependencies [[criterium "0.4.1"]
                                  [org.clojure/clojure "1.5.1"]]}}
  :warn-on-reflection true
  :jvm-opts ^:replace ["-server"]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.5" "-source" "1.5"])
