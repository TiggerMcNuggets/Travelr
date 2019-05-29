/*
 * Turns google maps values into readable values
 */
export const toTitleCase = (s) => {
    return s
        .replace(/([^A-Z])([A-Z])/g, '$1 $2') // split cameCase
        .replace(/[_-]+/g, ' ') // split snake_case and lisp-case
        .toLowerCase()
        .replace(/(^\w|\b\w)/g, function(m) { return m.toUpperCase(); }) // title case words
        .replace(/\s+/g, ' ') // collapse repeated whitespace
        .replace(/^\s+|\s+$/, ''); // remove leading/trailing whitespace
}