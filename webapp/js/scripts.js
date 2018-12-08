// $(".qna-comment").on("click", ".answerWrite input[type=submit]", addAnswer);
$(".answerWrite input[type=submit]").click(addAnswer);
$(".qna-comment").on("click", ".form-delete", deleteAnswer);


function addAnswer(e) {
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type : 'post',
    url : '/api/qna/addAnswer',
    data : queryString,
    dataType : 'json',
    error: onError,
    success : onSuccess,
  });
}

function onSuccess(json, status){
  var answer = json.answer;
  var count = json.answerCount;
  var answerTemplate = $("#answerTemplate").html();
  var template = answerTemplate.format(answer.writer, new Date(answer.createdDate), answer.contents, answer.answerId, answer.answerId);
  $(".qna-comment-slipp-articles").prepend(template);
  $("p.qna-comment-count strong").html(count);
}

function onError(xhr, status) {
  alert("error");
}

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};


function deleteAnswer(e) {
  e.preventDefault();

  var deleteBtn = $(this);
  var queryString = deleteBtn.closest("form").serialize();

  $.ajax({
           type : 'post',
           url : '/api/qna/deleteAnswer',
           data : queryString,
           dataType : 'json',
           error: onError,
           success : function(json, status) {
             var count = $("p.qna-comment-count strong").text();

             if (json.result) {
               $("p.qna-comment-count strong").html(count-1);
               deleteBtn.closest('article').remove();
             }
           },
         });
}

function onSuccessDelete(json, status) {
  var count = $("p.qna-comment-count strong").text();

  if (json.result) {
    $("p.qna-comment-count strong").html(count-1);
    $(this).closest('article').remove();
  }
}