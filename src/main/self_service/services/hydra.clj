(ns self-service.services.hydra
  (:require
   [clojure.core.async :as async]
   [self-service.components.configuration :refer [configuration]]
   [org.httpkit.client :as http]
   ))

(defn- hydra-configuration [] (::configuration configuration))

(defn default-options
  []
  (let [{:keys [mock-tls-termination]} (hydra-configuration)]
    (if mock-tls-termination
      {:headers {"X-Forwarded-Proto" "https"}}
      {}
      )))

(defn get-login-request
  [challenge]
  (let [{:keys [hydra-url]} (hydra-configuration)
        url                 (str hydra-url "/oauth2/auth/requests/login")]
    (http/get url (default-options))
    ))

(defn accept-login-request [challenge body])

(defn reject-login-request [challenge body])

(defn get-consent-request [challenge]
  (let [{:keys [hydra-url]} (hydra-configuration)
        url                 (str hydra-url "/oauth2/auth/requests/consent")]
    (http/get url (default-options))
    )
  )

(defn accept-consent-request [challenge body])

(defn reject-consent-request [challenge body])

(defn get-logout-request [challenge]
  (let [{:keys [hydra-url]} (hydra-configuration)
        url                 (str hydra-url "/oauth2/auth/requests/logout")]
    (http/get url (default-options))
    )
  )

(defn accept-logout-request [challenge body])

(defn reject-logout-request [challenge body])

(comment
  (async/go
    (println (async/<! (get-login-request "cat")))
    )

  )
