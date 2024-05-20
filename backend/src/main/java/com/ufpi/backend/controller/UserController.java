package com.ufpi.backend.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufpi.backend.config.response.ResponseModel;
import com.ufpi.backend.model.dto.UserCreateDTO;
import com.ufpi.backend.model.dto.UserDTO;
import com.ufpi.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ResponseModel<UserDTO>> cadastrar(UserCreateDTO userDTO) {
    var user = userService.insert(userDTO);
    ResponseModel<UserDTO> resposta = new ResponseModel<>();
    resposta.setData(UserDTO.fromEntity(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<ResponseModel<UserDTO>> consultaPaginada(
      @PageableDefault(size = 10, sort = { "dataCadastro" }, direction = Sort.Direction.DESC) Pageable paginacao) {
    var resultado = userService.consultar(paginacao);
    ResponseModel<UserDTO> resposta = new ResponseModel<>();
    resposta.setList(resultado.getContent().stream().map(UserDTO::fromEntity).toList());
    resposta.setTotalElements(resultado.getTotalElements());
    resposta.setTotalPages(resultado.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }
}
