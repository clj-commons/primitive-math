(defproject org.clj-commons/primitive-math
    (or (System/getenv "PROJECT_VERSION") "1.0.0")
  :description "Predictable, primitive math"
  :url "https://github.com/clj-commons/primitive-math"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies []
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases true}]]
  :profiles {:dev {:dependencies [[criterium "0.4.6"]
                                  [org.clojure/clojure "1.10.0"]]}}
  :global-vars {*warn-on-reflection* true}
  :jvm-opts ^:replace ["-server"]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.8" "-source" "1.8"])
