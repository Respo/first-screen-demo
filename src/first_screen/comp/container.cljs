
(ns first-screen.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]))

(def style-header
  {:align-items :center,
   :background-color colors/motif,
   :padding 16,
   :transition-duration "360ms",
   :height 240})

(def style-entry {:min-width 120, :color :white, :font-size 14, :padding "0 16px"})

(defn render-entry [title] (div {:style style-entry} (comp-text title nil)))

(def style-logo
  {:background-color (hsl 0 0 100), :width 160, :transition-duration "360ms", :height "160"})

(def style-welcome {:color :white, :font-size 40})

(defn render-header [loaded?]
  (div
   {:style (merge ui/row style-header (if loaded? {:height 60}))}
   (div {:style (merge style-logo (if loaded? {:width 40, :height "40"}))})
   (comp-space 16 nil)
   (if loaded?
     (div {:style ui/row} (render-entry "Home") (render-entry "Data list"))
     (div {:style style-welcome} (comp-text "Wellcome visiting!")))))

(defn render [store ssr-stages]
  (fn [state mutate!]
    (let [loaded? (get-in store [:loading :content?])]
      (div {:style (merge ui/global)} (render-header loaded?)))))

(def comp-container (create-comp :container render))
