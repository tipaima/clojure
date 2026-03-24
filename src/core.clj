(ns core)

(def fib
  (let [cache (atom {0 0, 1 1})]
    (with-meta
      (fn fib
        ([n] (fib n cache))
        ([n cache-ref]
         (if-let [cached (get @cache-ref n)]
           cached
           (let [result (+ (fib (- n 1) cache-ref)
                           (fib (- n 2) cache-ref))]
             (swap! cache-ref assoc n result)
             result))))
      {:cache cache})))

(defn get-fib-cache []
  @(-> fib meta :cache))

(defn read-number []
  (loop []
    (println "Введите номер числа Фибоначчи: \n")
    (flush)
    (let [input (read-line)]
      (if-let [num (try
                     (parse-long input)
                     (catch Exception _ nil))]
        (if (>= num 0)
          num
          (do
            (println "Ошибка! Число должно быть >= 0")
            (recur)))
        (do
          (println "Ошибка! Введите целое число")
          (recur))))))

(defn main []
  (println "\n=======================================")
  (loop []
    (println "1. Продолжить")
    (println "2. Выход")
    (println "Выбор: \n")
    (flush)
    (let [choice (read-line)]
      (case choice
        "1" (let [num (read-number)]
              (if num
                (let [result (time (fib num))]
                  (println (format "\nFib(%d) = %d" num result)))
                (let [cache (get-fib-cache)
                      sorted-keys (sort (keys cache))]
                  (doseq [n sorted-keys]
                    (println (format "  fib(%2d) = %d" n (get cache n))))))
              (println "\n--------------")
              (recur))
        "2" (println "\nВыход из программы!")
        (do
          (println "Неверный выбор. Попробуйте снова.")
          (recur)))))
  (println "----------------"))

(comment
  (main))