(ns self-service.core
  (:require
   [mount.core :as mount]
   )
  (:gen-class))

(defn -main [& args]
  (mount/start-with-args {:config "config/prod.edn"}))
