(ns space-is-the-place.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [overtone.studio.midi :refer [midi-connected-receivers]]
            [overtone.midi :as overtone.midi]))

(defn midi-receiver []
  (first (midi-connected-receivers)))

(defn linear-scan [{:keys [x y]}
                   {:keys [w h]}]
  ; (println " x " x " y " y " w " w " h " h)
  (cond
    (and (>= x w)
         (>= y h))
    {:x 0 :y 0}

    (>= x w)
    {:x 0
     :y (inc y)}

    :else
    {:x (inc x)
     :y y}))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  (let [url "resources/4x4.png"
        img (q/load-image url)]
    {:color      0
     :bg-color   -1
     :image      img
     :midi-out   (midi-receiver)
     :canvas     {:h (q/height)
                  :w (q/width)}
     :last-color nil
     :cursor     {:x 0 :y 0}}))

(defn play-midi [state]
  ;; Play a midi note c4 at 80 velocity for 1 millisecond on the fourth channel
  ;; Note that the channel is zero-indexed, whereas normal mixers/midi devices start counting them from 1.
  (let [midi (:midi-out state)
        note 64]
    (if midi
      (do (overtone.midi/midi-note midi note 80 1 3)
          (println "playing to midi"))
      (println "tried playing; no midi"))))

(defn color-at-cursor [{:keys [cursor] :as state}]
  (let [im (q/state :image)]
    (when (q/loaded? im)
      (q/get-pixel im (:x cursor) (:y cursor)))))

(defn trigger? [state]
  (let [current-color (color-at-cursor state)]
    (and (not (= current-color (:bg-color state)))
         (not (= current-color (:last-color state))))))

(defn update-state [state]
  (let [state' (-> state
                   (assoc :last-color (color-at-cursor state))
                   (update :cursor #(linear-scan % (:canvas state))))]
    (if (trigger? state')
      (play-midi state')
      (println "skipping"))
    state'))

(defn draw-state [{:keys [cursor] :as state}]
  (q/fill (:color state) 255 255)
  (let [img (q/state :image)]
    (when (q/loaded? img)
      (q/image img 0 0)))
  (q/ellipse (:x cursor) (:y cursor) 10 10))


(q/defsketch sekt
  :title "You spin my circle right round"
  :size [32 32]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
