function Validate(formSelector){
    function getParent(element, selector){
        while (element.parentElement){
            if(element.parentElement.matches(selector)){
                return element.parentElement;
            }
            element = element.parentElement;
        }
    }

    var formRules = {};
    var validatorRules = {
        required: function(value){
            return value ? undefined : 'Vui lòng nhập trường này';
        },
        email: function(value){
            var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
            return regex.test(value) ? undefined : 'Vui lòng nhập đúng email';
        },
        min: function(min){
            return function(value){
                return value.length >= min ? undefined : `Vui lòng nhập ít nhất ${min} ký tự`;
            }
        },
        phoneNumber: function(value){
            return (parseInt(value) > 0) && (typeof parseInt(value) === 'number') && (value.length == 10) ? undefined : 'Số điện thoại không tồn tại';
        },
        comfirmed: function(value){
            var valuePass = document.getElementById('password').value;
            // return function(value){
            //     return value === newpass ? undefined : 'Xác nhận không chính xác';
            // }
            if(value == valuePass){
                return undefined;
            }
            return 'Xác nhận không chính xác';
        },
    };

    var formElement = document.querySelector(formSelector);

    if(formElement){

        var inputs = formElement.querySelectorAll('[name][data-rule]');
        for(var input of inputs){
            var rules = input.getAttribute('data-rule').split('|');
            for(var rule of rules){
                var ruleHasValue = rule.includes(':');
                var ruleInfo;
                if(ruleHasValue){
                    ruleInfo = rule.split(':');
                    rule = ruleInfo[0];
                }

                var ruleFunc = validatorRules[rule];
                if(ruleHasValue){
                    ruleFunc = ruleFunc(ruleInfo[1]);
                }

                if(Array.isArray(formRules[input.name])){
                    formRules[input.name].push(ruleFunc);
                }else{
                    formRules[input.name] = [ruleFunc];
                }   
            }

            //event
            input.onblur = handleValidate;
            input.oninput = handleClearError;
        }

        console.log(formRules);

        function handleValidate(event){
            var rules = formRules[event.target.name];
            var errorMessage;

            rules.find(function (rule) {
                if (typeof rule === 'function') {
                    errorMessage = rule(event.target.value);
                    return errorMessage;
                }
            });
            console.log(errorMessage);

            if(errorMessage){
                var formGroup = getParent(event.target, '.form-input');
                formGroup.querySelector('input').classList.add('invalid');
                if(formGroup){
                    var formMessage = formGroup.querySelector('.formError');
                    if(formMessage){
                        formMessage.innerText = errorMessage;
                    }
                }
            }

            return !errorMessage;
        }

        function handleClearError(event){
            var formGroup = getParent(event.target, '.form-input');
            formGroup.querySelector('input').classList.remove('invalid');
                if(formGroup){
                    var formMessage = formGroup.querySelector('.formError');
                    if(formMessage){
                        formMessage.innerText = '';
                    }
                }
        }

        formElement.onsubmit = function (event){
            event.preventDefault();

            var inputs = formElement.querySelectorAll('[name][data-rule]');
            var invalid = true;
            
            for(var input of inputs){
                if(!handleValidate({ target : input })){
                    invalid = false;
                }
            }
        }
        
    }
}