$(document).ready(function() {

    // Обновление данных на главной странице
    function updateMainPage(data) {
        $(".masha").val(data);
        console.log(data);
    }



    // Получение данных с сервера
    function receive() {
        $.ajax({
            method: 'POST',
            url: '/getCurrentState',
            data: {},
            success: function(data) {
                updateMainPage(data);
            }
        });
    }


    // Запуск таймера
    var timerId = setTimeout(function tick() {
        receive();
        timerId = setTimeout(tick, 5000);
    }, 5000);

});