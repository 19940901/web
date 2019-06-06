$(document).ready(function () {
    var l = ["../images/girl.jpg", "../images/girl.jpg", "../images/beauty.png", "../images/girl.jpg", "../images/tulip2.png", "../images/5.png", "../images/tulip1.png", "../images/tulip1.png", "../images/tulip1.png", "../images/tulip1.png", "../images/tulip1.png", "../images/tulip1.png"];
    $.each(l, function (i, item) {

        var child = "<div class=\"col-lg-3 col-md-4 col-sm-6 col-xs-12\"> <div class=\"hovereffect\"> <img class=\"img-responsive\" src=" + item + " alt=\"error\" style='max-height: 100%;max-width: 100%'> <div class=\"overlay\"> <h2>Effect 14</h2> <p> <a href=\"#\">LINK HERE</a> </p> </div> </div> </div>"
        $('#parentC').append(child);

    });
});


function isHasImg(pathImg) {
    var ImgObj = new Image();
    ImgObj.src = pathImg;
    if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
        return true;
    } else {
        return false;
    }

}
