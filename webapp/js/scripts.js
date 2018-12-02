String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(".answerWrite input[type=submit]").click(addAnswer);
$(".article-util button[type=submit]").click(deleteAnswer);
// $(".qna-comment").click(deleteAnswer);

function addAnswer(e) {
  e.preventDefault();
  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type : 'post',
    url : '/api/qna/addAnswer',
    data : queryString,
    dataType : 'json',
    error : onError,
    success : onSuccess,
         });
}

function deleteAnswer(e) {
  e.preventDefault();
  var queryString = $("input[name=answerId]").serialize()

  $.ajax({
    type: 'post',
    url : '/api/qna/deleteAnswer',
    data: queryString,
    dataType: 'json',
    error: onError,
    success: onSuccessDeleteAnswer,
         })
}

function onSuccess(json, status) {
  var answerTemplate = $("#answerTemplate").html();
  var template = answerTemplate.format(json.writer, new Date(json.createDate), json.contents, json.answerId);
  $(".qna-comment-slipp-articles").prepend(template);
}

function onSuccessDeleteAnswer(json, status) {
  var resultStatus = json.status
  if(resultStatus) {
    $(this).closest('artice').remove();
  } else {

  }
}

function onError() {
  // alert('woops!');
}

