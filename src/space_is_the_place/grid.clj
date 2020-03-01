(ns space-is-the-place.grid
  (:require [overtone.osc :as osc]))


;(defonce grid (mcore/connect "/dev/tty.usbserial-m1000548"))
;
;
;grid

;(:led-set-all grid 12)
;( grid "/grid/led/set 12")
;(mled/led-on grid 0 0 15)

(defonce server (osc/osc-server 12003))
(defonce oscserial (osc/osc-client "localhost" 12002))
;(def osculator (osc/osc-client "localhost" 8000))
(def grid (osc/osc-client "localhost" 17471))

;(osc/osc-listen grid (fn [msg] (println "listener: " msg)) :foo)
;
;(osc/osc-send-msg oscserial (osc/osc-msg "/serialosc/info" "localhost" 12003))

;(osc/osc-listen server (fn [msg] (println "listener: " msg)) :foo)
;(osc/osc-handle server (fn [msg] (println "listener: " msg)) :foo)
;
;(osc/osc-send-msg grid {:path "grid/led/set" :type-tag "i i i" :args [0 0 15]})
(osc/osc-send-msg grid (osc/osc-msg "/sys/info" "localhost" 8000))
(osc/osc-send-msg grid (osc/osc-msg "/sys/port" 12003))
(osc/osc-send-msg grid (osc/osc-msg "/sys/prefix" "j"))
(osc/osc-send-msg grid (osc/osc-msg "/j/grid/led/level/set" 0 0 14))
(osc/osc-send-msg grid (osc/osc-msg "/j/grid/led/level/set" 2 2 14))

(defn set-led [args])

(osc/osc-handle server "/j/grid/key" (fn [{[x y z] :args}] (prn "hello" x y z)))

;(osc/osc-send-msg osculator (osc/osc-msg "grid/led/all" 15))
;(osc/osc-send-msg osculator {:path "foo" :type-tag "i" :args [42]})
;


