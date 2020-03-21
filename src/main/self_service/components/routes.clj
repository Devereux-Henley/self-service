(ns self-service.components.routes
  (:require
   [self-service.routes.login :as login-routes]
   [mount.core :refer [defstate]]
   ))

(defstate routes
  :start
  [
   login-routes/routes
   ]
  )
