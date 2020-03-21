(ns self-service.components.configuration
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [mount.core :refer [defstate args]]
   [taoensso.timbre :as log]
   ))

(defn configure-logging! [config]
  (let [{:keys [taoensso.timbre/logging-config]} config]
    (log/info "Configuring Timbre with " logging-config)
    (log/merge-config! logging-config)))

;; Configuration loading shamelessly stolen from: https://github.com/fulcrologic/fulcro/blob/5435fe74501c9acf1783fd7832f0f2a10d953ee9/src/main/com/fulcrologic/fulcro/server/config.clj

(defn get-system-prop [prop-name]
  (System/getProperty prop-name))

(defn- load-edn!
  "If given a relative path, looks on classpath (via class loader) for the file, reads the content as EDN, and returns it.
  If the path is an absolute path, it reads it as EDN and returns that.
  If the resource is not found, returns nil.
  This function returns the EDN file without further interpretation (no merging or env evaluation).  Normally you want
  to use `load-config!` instead."
  [^String file-path]
  (let [?edn-file (io/file file-path)]
    (if-let [edn-file (and (.isAbsolute ?edn-file)
                        (.exists ?edn-file)
                        (io/file file-path))]
      (-> edn-file slurp edn/read-string)
      (some-> file-path io/resource .openStream slurp edn/read-string))))

(defn- load-edn-file!
  "Calls load-edn on `file-path`,
  and throws an ex-info if that failed."
  [file-path]
  (log/info "Reading configuration file at " file-path)
  (if-let [edn (some-> file-path load-edn!)]
    edn
    (do
      (log/error "Unable to read configuration file " file-path)
      (throw (ex-info (str "Invalid config file at '" file-path "'")
               {:file-path file-path})))))

(defstate configuration
  :start
  (let [{:keys [configuration-path] :or {configuration-path "configuration/dev.edn"}} (args)
        configuration                                                                 (load-edn-file! configuration-path)]
    (log/info "Loaded configuration" configuration)
    (configure-logging! configuration)
    configuration))
