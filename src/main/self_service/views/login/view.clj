(ns self-service.views.login.view
  (:require
   [self-service.views.common :refer [page]]
   [hiccup.form :as form]
   [ring.util.anti-forgery :as anti-forgery-util]
   ))

(defn view [{:keys [challenge]}]
  (page
    [:div {:class "ui full-height middle aligned center aligned grid"}
     [:div {:class "column"}
      [:h2 {:class "ui header"}
       [:div {:class "content"} "Log in to your account."]
       ]
      (form/form-to {:class "ui large form"} [:post "/login"]
        [:div {:class "ui stacked segment"}
         (anti-forgery-util/anti-forgery-field)
         (form/hidden-field "challenge" challenge)
         [:div {:class "field"}
          (form/email-field {:class "ui input" :placeholder "email@test.com"} "email")
          ]
         [:div {:class "field"}
          (form/password-field {:class "ui input"} "password")]
         ]
        [:div {:class "ui fluid large submit button"} "Login"]
        )]
     ]))
