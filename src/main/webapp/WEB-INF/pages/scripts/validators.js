/* Validators */

function numberValidator(self) {
    var value = self.value;
    if (+value < +$(self).attr('min') || +value > +$(self).attr('max')) {
        $(self).addClass("_error");
    } else {
        $(self).removeClass("_error");
    }
}