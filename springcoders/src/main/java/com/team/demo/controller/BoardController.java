package com.team.demo.controller;

import com.team.demo.domain.dto.BoardDTO;
import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.FileDTO;
import com.team.demo.domain.dto.PageDTO;
import com.team.demo.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	@Autowired
	private BoardService service;

	@GetMapping("write")
	//model.addAttribute() 를 바로 해줌
	public void write(@ModelAttribute("cri") Criteria cri, Model model) { }

	@GetMapping("list")
	public void list(Criteria cri, Model model) {
		System.out.println("cri = " + cri);
		cri.setAmount(20);
		System.out.println("Criteria 확인 : "+cri);
		List<BoardDTO> list = service.getList(cri);
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", new PageDTO(service.getTotal(cri),cri, 10));
	}


	@PostMapping("regist")
	public String regist(BoardDTO board, MultipartFile[] files) throws Exception {
		Criteria cri = new Criteria();
		cri.setType("");
		cri.setKeyword("");
		cri.setStartrow(0);
		if(service.regist(board, files)) {
			long boardnum = service.getLastNum(board.getUserid());
			return "redirect:/board/get"+cri.getListLink()+"&boardnum="+boardnum;
		} else {
			return "redirect:/board/list"+cri.getListLink();
		}
	}

	@GetMapping(value={"get", "modify"})
	public String get(Criteria cri, long boardnum, HttpServletRequest req, HttpServletResponse resp, Model model) {
		String requestURI = req.getRequestURI();
		model.addAttribute("cri",cri);
		HttpSession session = req.getSession();
		BoardDTO board = service.getDetail(boardnum);
		model.addAttribute("board",board);

//		판매완료된 상품인지 조회 시작

		String buystate = service.getBuyState(boardnum);
		model.addAttribute("buystate",buystate);
//		판매완료된 상품인지 조회 끝
//      ---------------- recent(최근 등록된 글)의 BoardDTO 와 file 출력 시작 ---------------------
		List<BoardDTO> originalList = service.getList(cri);
		List<BoardDTO> subList = new ArrayList<>();
		List<List<FileDTO>> filelist = new ArrayList<>();
		filelist.add(service.getFiles(boardnum));
		for(int i=0;i<(originalList.size()>4?4:originalList.size());i++) {
			if(board.getBoardnum()==originalList.get(i).getBoardnum()) {
				continue;
			}
			BoardDTO bdto = originalList.get(i);
			subList.add(bdto);
			filelist.add(service.getFiles(bdto.getBoardnum()));
		}
		model.addAttribute("list",subList);


		model.addAttribute("files",filelist);
		model.addAttribute("pageMaker", new PageDTO(service.getTotal(cri),cri, 10));
		System.out.println("board = " + board);
		System.out.println("model.getAttribute(\"list\") = " + model.getAttribute("list"));

//      --------------------- recent(최근 등록된 글)의 BoardDTO 와 file 출력 끝 -------------------


		String loginUser = (String)session.getAttribute("loginUser");

//      System.out.println("cri = " + cri);
//      System.out.println("board = " + board);
//      System.out.println("files = " + service.getFiles(boardnum));
//      System.out.println("subList = " + subList);
		System.out.println("filelist = " + filelist);
		if(requestURI.contains("modify")) {
			return "board/modify";
		}

		return "board/get";
	}
	@GetMapping("remove")
	public String remove(Criteria cri, long boardnum,HttpServletRequest req) {
		System.out.println(" 여기 삭제" );
		String loginUser = (String)req.getSession().getAttribute("loginUser");
		BoardDTO board = service.getDetail(boardnum);
		if(loginUser.equals(board.getUserid())) {
			if(service.remove(boardnum)) {
				return "redirect:/board/list"+cri.getListLink();
			}
		}
//		System.out.println(" 삭제 실패");

		return "redirect:/board/get"+cri.getListLink()+"&boardnum="+boardnum;

	}

// 결제하기 시작
	@PostMapping(value = "/boardPay", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> boardPay(@RequestParam long boardnum, HttpServletRequest req ) {
		HttpSession session = req.getSession();
		String loginUser = (String)session.getAttribute("loginUser");
		if(service.checkBuyState(boardnum)){
			System.out.println("service.checkBuyState(boardnum) = " + service.checkBuyState(boardnum));
			return new ResponseEntity<String>("Y", HttpStatus.OK);
		}
		else{
			if(service.boardPayment(loginUser, boardnum)) {
				return new ResponseEntity<String>("O", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("X", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
// 결제하기 끝

}