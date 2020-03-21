(ns self-service.views.login.styles
  (:require
   [garden.def :refer [defstylesheet]]
   ))

(defstylesheet stylesheet
  [
   :body {:height "100%"}
   ]
  [
   :.full-height {:height "100%"}
   ]
  [
   :.column {:max-width "455px"}
   ]
  )
