(ns self-service.components.server
  (:require
   [self-service.components.configuration :refer [configuration]]
   [self-service.components.middleware :refer [middleware]]
   [clojure.pprint :refer [pprint]]
   [mount.core :refer [defstate]]
   [org.httpkit.server :as http-kit]
   [taoensso.timbre :as log]
   ))

(defstate server
  :start (let [server-configuration (::http-kit/configuration configuration)]
           (log/info "Starting HTTP Server with configuration " (with-out-str (pprint server-configuration)))
           (http-kit/run-server middleware server-configuration))
  :stop (server))
