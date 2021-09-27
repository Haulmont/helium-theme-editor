io_jmix_editor_helium_components_themevariablefield_ThemeVariableField = function () {
    this.setThemeVariable = function (variableName, value) {
        Array.from(document.getElementsByClassName('helium'))
            .forEach(function (element) {
                element.style.setProperty(variableName, value)
            });
    }

    this.removeThemeVariable = function (variableName) {
        Array.from(document.getElementsByClassName('helium'))
            .forEach(function (element) {
                element.style.removeProperty(variableName)
            });
    };
};