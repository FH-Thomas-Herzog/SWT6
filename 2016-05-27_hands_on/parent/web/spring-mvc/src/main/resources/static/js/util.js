(function (_$, _window) {
    var $ = _$;

    this.formSubmitHandler = function (_form, _content) {
        var
            form = $(_form),
            content = $('#' + _content);
        $.ajax({
            url: form.attr('action'),
            type: form.attr('method'),
            data: form.serialize(),
            success: function (response) {
                content.html(response);
            }
        });
    };

    this.ajaxInvoker = function (_action, _method, _data, _update) {
        $.ajax({
            url: _action,
            type: _method,
            data: _data,
            success: function (response) {
                $('#' + _update).html(response);
            }
        });
    };

    this.postInvoker = function (_action, _data, _update) {
        this.ajaxInvoker(_action, 'post', _data, _update);
    }

    this.getInvoker = function (_action, _data, _update) {
        this.ajaxInvoker(_action, 'get', _data, _update);
    }

    _window.EM = this;
})($, window);