(ns space-is-the-place.hilbert
  (:require []))

;; in draw
;; (hilbert start-x start-y canvas-w 0 0 canvas-h levels)

(defn hilbert [x0 y0 xi xj yi yj level]
  (if (<= level 0)
    (let [x (+ x0 (/ (+ xi yi) 2))
          y (+ y0 (/ (+ xj yj) 2))]
      (q/ellipse x y 2 2))
    (let [a (hilbert x0 y0
                     (/ yi 2) (/ yj 2)
                     (/ xi 2) (/ xj 2) (dec level))
          b (hilbert (+ x0 (/ xi 2)) (+ y0 (/ xj 2))
                     (/ xi 2) (/ xj 2)
                     (/ yi 2) (/ yj 2) (dec level))
          c (hilbert (+ x0 (/ xi 2) (/ yi 2)) (+ y0 (/ xj 2) (/ yj 2))
                     (/ xi 2) (/ xj 2)
                     (/ yi 2) (/ yj 2) (dec level))
          d (hilbert (+ x0 (/ xi 2) yi) (+ y0 (/ xj 2) yj)
                     (- (/ yi 2)) (- (/ yj 2))
                     (- (/ xi 2)) (- (/ xj 2)) (dec level))]
      (print (:path state)))))
