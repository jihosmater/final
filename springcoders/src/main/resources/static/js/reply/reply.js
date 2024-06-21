const replyService = {
	insert:function(data,callback,error){
		console.log("전송 :",data);
		$.ajax({
			type:"POST",
			url:"/reply/regist?placeid="+contentid,
			data:JSON.stringify(data),
			contentType:"application/json;charset=utf-8",
			success:function(result,status,xhr){
				callback(result)
			},
			error:function(result,status,xhr){
				error(result)
			}
		})
	},
	selectAll:function(data,callback){
		let replynum = data.replynum;
		let contentid = data.contentid;

		$.getJSON(
			`/reply/list/${replynum}/${contentid}`,
			function(replyList){
				//data : 응답되는 JSON ({ replyCnt:댓글갯수, list:[...] })
				// callback(data.replyCnt, data.list);
				console.log(replyList);
				callback(replyList);
			}
		)
	},
	delete:function(replynum,callback,error){
		$.ajax({
			type:"DELETE",
			url:`/reply/${replynum}`,
			success:function(result){
				console.log(result);
				callback(result);
			},
			error:function(result){
				console.log(result);
				error(result);
			}
		})
	}
	// update:function(data,callback){
	// 	$.ajax({
	// 		type:"PUT",
	// 		url:"/reply/"+data.replynum,
	// 		data:JSON.stringify(data),
	// 		contentType:"application/json;charset=utf-8",
	// 		success:function(result){
	// 			callback(result);
	// 		}
	// 	})
	// }
}











