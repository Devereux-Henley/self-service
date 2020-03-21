(ns self-service.components.middleware
  (:require
   [self-service.components.configuration :refer [configuration]]
   [self-service.components.routes :refer [routes]]
   [mount.core :refer [defstate]]
   [muuntaja.core]
   [reitit.ring :as ring]
   [reitit.ring.spec :as spec]
   [reitit.coercion.spec]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.exception :as exception]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :as anti-forgery]
   ))

(defstate middleware
  :start
  (ring/ring-handler
    (ring/router
      routes
      {
       :validate spec/validate
       :data     {
                  :coercion reitit.coercion.spec/coercion
                  :muntaaja muuntaja.core/instance
                  :middleware
                  [
                   ;; query-params & form-params
                   parameters/parameters-middleware
                   ;; content-negotiation
                   muuntaja/format-negotiate-middleware
                   ;; encoding response body
                   muuntaja/format-response-middleware
                   ;; exception handling
                   exception/exception-middleware
                   ;; decoding request body
                   muuntaja/format-request-middleware
                   ;; coercing response bodys
                   coercion/coerce-response-middleware
                   ;; coercing request parameters
                   coercion/coerce-request-middleware
                   ;; multipart
                   multipart/multipart-middleware
                   ;; anti forgery
                   anti-forgery/wrap-anti-forgery
                   ]}}
      )
    (ring/create-default-handler)))
