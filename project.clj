(defproject primitive-math "0.1.0-SNAPSHOT"
  :description "predictable, primitive math"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[robert/hooke "1.3.0"]]
  :profiles {:dev {:dependencies [[criterium "0.4.1"]
                                  [org.clojure/clojure "1.5.1"]]}}
  :jvm-opts ^:replace ["-server"]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.5" "-source" "1.5"])
