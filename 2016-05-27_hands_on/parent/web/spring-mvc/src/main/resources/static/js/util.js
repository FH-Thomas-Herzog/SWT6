(function (_$, _window) {
    var $ = _$;

    this.submitForm = function (_form, _content) {
        var form = $('#' + _form);
        form.on('submit', function (e) {
            e.preventDefault();
            $.ajax({
                url: form.attr('action'),
                type: form.attr('method'),
                data: form.serialize(),
                success: function (response) {
                    $('#' + _content).html(response);
                }
            });
        });
    }
    console.log("initialized");
    _window.EM = this;
})($, window);