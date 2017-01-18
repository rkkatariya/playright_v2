/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//output date format dd-mm-yyyy
function convertDate(inputFormat, defaultToday) {
    function pad(s) { return (s < 10) ? '0' + s : s; }
    if (inputFormat !== null && inputFormat !== "" && typeof inputFormat !== "undefined") {
        var d = new Date(inputFormat);
        return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('-');
    } else if (defaultToday === true) {
        var d = new Date();
        return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('-');
    } else {
        return "";
    }
}