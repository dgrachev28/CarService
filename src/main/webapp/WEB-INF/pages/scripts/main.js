$(document).ready(function() {

    // Константы
    var TIMER_DELAY = 5000;

    // Переменные
    var timerId;



    //Обработчики событий

    // Пауза / запуск
    $(".content__button").on("click", function() {
        var $this = $(this);
        if ($this.hasClass("content__button_play")) {
            $this.removeClass("content__button_play");
            timerStart();
        } else {
            $this.addClass("content__button_play");
            clearTimeout(timerId);
        }
    });



    // Обновление данных на главной странице
    function updateMainPage(data) {
        $(".masha").val(data);
        console.log(data);
    }



    // Получение данных с сервера
    function receive() {
        $.ajax({
            method: 'GET',
            url: '/getCurrentState',
            accepts: "application/json; charset=utf-8",
            data: {},
            success: function(data) {
                updateMainPage(data);
            }
        });
    }


    // Запуск таймера
    function timerStart() {
        timerId = setTimeout(function tick() {
            receive();
            timerId = setTimeout(tick, TIMER_DELAY);
        }, TIMER_DELAY);
    }



    // Начало работы
    timerStart();

});