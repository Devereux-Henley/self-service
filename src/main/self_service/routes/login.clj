(ns self-service.routes.login
  (:require
   [self-service.api.login.biz :as login-biz]
   [self-service.views.login.view :as login-view]
   [self-service.views.login.styles :as login-styles]
   [taoensso.timbre :as log]
   )
  )

(defn login-view-handler [{{{:keys [login_challenge]} :query} :parameters}]
  (log/debug "Serving login view.")
  {
   :status  200
   :headers {"Content-Type" "text/html"}
   :body    (login-view/view {:challenge login_challenge})
   }
  )

(defn login-redirect-handler [{:keys [uri query-string]}]
  (log/debug "Redirecting to login view.")
  {
   :status 301
   :headers {
             "Location" (str uri "/index.html" (when query-string (str "?" query-string)))
            }
   :body ""
   })

(defn login-styles-handler [parameters]
  (log/debug "Serving login styles.")
  {
   :status  200
   :headers {"Content-Type" "text/css"}
   :body    login-styles/stylesheet
   }
  )

(def routes
  ["/login"
   [
    "/index.html"
    {:get {:summary    "Serves login view."
           :parameters {:query {:login_challenge string?}}
           :handler    login-view-handler}}
    ]
   [
    "/stylesheet.css"
    {:get {:summary "Serves login stylesheet."
           :handler login-styles-handler}}
    ]
   [
    ""
    {:get {:summary    "Serves login view."
           :parameters {:query {:login_challenge string?}}
           :handler    login-redirect-handler}}
    ]
   ]
  )
