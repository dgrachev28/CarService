$(document).ready(function() {

    var classes = {
        contentButton: "content__button",
        contentButtonPlay: "content__button_play",
        workshop: "workshop",
        workshopMasters: "workshop-masters",
        workshopMastersItem: "workshop-masters__item",
        workshopQueue: "workshop-queue",
        workshopQueueItem: "workshop-queue__item"
    };



    var constants = {
        TIMER_DELAY: 5000
    };



    var timerId,
        $contentButton,
        $workshop;




    function initVars() {
        $contentButton = $("." + classes.contentButton);
        $workshop = $("." + classes.workshop);
    }


    function bindEvents() {
        $contentButton.on("click", playButtonHandler);
    }



    function playButtonHandler() {
        var $this = $(this);
        if ($this.hasClass(classes.contentButtonPlay)) {
            $this.removeClass(classes.contentButtonPlay);
            timerStart();
        } else {
            $this.addClass(classes.contentButtonPlay);
            clearTimeout(timerId);
        }
    }



    function updateContent(data) {
        var i, j;

        // Removing content
        $("." + classes.workshopQueueItem).remove();
        $("." + classes.workshopMastersItem).remove();



        if (data && data.workshops) {
            var workshops = data.workshops;
            for (i = 0; workshops && i < workshops.length; ++i) {
                var id = workshops[i].id;

                var markupWorkshop = $("#workshop" + id);

                // Update queue
                var queue = workshops[i].queue;
                var markupQueue = markupWorkshop.find("." + classes.workshopQueue);
                for (j = 0; j < queue.length; ++j) {
                    markupQueue.append(getQueueItemMarkup(queue[j].id, queue[j].number, queue[j].service, queue[j].date));
                }

                //Update masters
                var masters = workshops[i].masters;
                var markupMasters = markupWorkshop.find("." + classes.workshopMasters);
                for (j = 0; j < masters.length; ++j) {
                    markupMasters.append(getMasterMarkup(masters[j].id, masters[j].name, masters[j].busy));
                }
            }
        }
    }

    // Helpers
    function getQueueItemMarkup(id, number, service, date) {
        return '<li class="workshop-queue__item" id="ticket' + id + '">' +
            '<span class="workshop-queue__item-number">' + number + '</span>' +
            '<span class="workshop-queue__item-service">' + service + '</span>' +
            '<span class="workshop-queue__item-date">' + date + '</span>' +
        '</li>';
    }

    function getMasterMarkup(id, name, busy) {
        function busyMarkup(busy) {
            if(busy) {
                return '<span class="workshop-masters__item-work">занят</span>';
            } else {
                return '<span class="workshop-masters__item-work workshop-masters__item-work_free">свободен</span>';
            }
        }

        return '<li class="workshop-masters__item" + id="master' + id + '">' +
            '<span class="workshop-masters__item-name">' + name + '</span>' +
            '<span class="workshop-masters__item-work">' + busyMarkup(busy) + '</span>' +
            '</li>';

    }



    // Получение данных с сервера
    function receive() {
        $.ajax({
            method: 'GET',
            url: '/getCurrentState',
            accepts: "application/json; charset=utf-8",
            data: {},
            success: function(data) {
                updateContent(data);
            }
        });
    }


    // Запуск таймера
    function timerStart() {
        timerId = setTimeout(function tick() {
            receive();
            timerId = setTimeout(tick, constants.TIMER_DELAY);
        }, constants.TIMER_DELAY);
    }



    function init() {

        initVars();

        bindEvents();

        updateContent({
            workshops: [
                {
                    id: 3,
                    queue: [],
                    masters: [
                        {
                            id: 17,
                            name: "Денис",
                            busy: false
                        },
                        {
                            id: 9,
                            name: 'Анна',
                            busy: true
                        },
                        {
                            id: 7,
                            name: "Сергей",
                            busy: true
                        }
                    ]
                },
                {
                    id: 1,
                    queue: [
                        {
                            id: 33,
                            number: "А789ОНР",
                            service: 'техосмотр',
                            date: '10:33 24/03'
                        }
                    ],
                    masters: []
                },
                {
                    id: 2,
                    queue: [
                        {
                            id: 1,
                            number: "Р777ПЕ",
                            service: 'покраска кузова',
                            date: '12:46 24/03'
                        }
                    ],
                    masters: []
                },
                {
                    id: 4,
                    queue: [
                        {
                            id: 53,
                            number: "П411РН",
                            service: 'ремонт двигателя',
                            date: '12:46 24/03'
                        },
                        {
                            id: 54,
                            number: "Н573ТЗ",
                            service: 'ремонт двигателя',
                            date: '15:23 24/03'
                        },
                        {
                            id: 55,
                            number: "П411РН",
                            service: 'замена масла',
                            date: '15:48 24/03'
                        }
                    ],
                    masters: []
                }
            ]
        });


        updateContent({
            workshops: [
                {
                    id: 3,
                    queue: [],
                    masters: [
                        {
                            id: 17,
                            name: "Денис",
                            busy: true
                        },
                        {
                            id: 9,
                            name: 'Анна',
                            busy: false
                        },
                        {
                            id: 7,
                            name: "Сергей",
                            busy: false
                        }
                    ]
                },
                {
                    id: 1,
                    queue: [],
                    masters: []
                },
                {
                    id: 2,
                    queue: [
                        {
                            id: 1,
                            number: "Р777ПЕ",
                            service: 'ремонт кузова',
                            date: '12:46 24/03'
                        }
                    ],
                    masters: []
                },
                {
                    id: 4,
                    queue: [
                        {
                            id: 53,
                            number: "П411РН",
                            service: 'ремонт двигателя',
                            date: '12:46 24/03'
                        },
                        {
                            id: 54,
                            number: "Н573ТЗ",
                            service: 'замена масла',
                            date: '15:23 24/03'
                        },
                        {
                            id: 55,
                            number: "П411РН",
                            service: 'замена масла',
                            date: '15:48 24/03'
                        }
                    ],
                    masters: []
                }
            ]
        });


        //$.ajax({
        //    method: 'POST',
        //    url: '/getCurrentState',
        //    data: {},
        //    success: function(data) {
        //        updateContent(data);
        //    }
        //});
    }



    // Начало работы
    init();

});