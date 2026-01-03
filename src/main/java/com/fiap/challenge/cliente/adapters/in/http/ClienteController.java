package com.fiap.challenge.cliente.adapters.in.http;

import com.fiap.challenge.cliente.adapters.in.http.dto.AuthRequestDTO;
import com.fiap.challenge.cliente.adapters.in.http.dto.AuthResponseDTO;
import com.fiap.challenge.cliente.adapters.in.http.dto.ClienteDTO;
import com.fiap.challenge.cliente.application.exception.ClienteCpfJaCadastradoException;
import com.fiap.challenge.cliente.application.service.JwtService;
import com.fiap.challenge.cliente.domain.entities.Cliente;
import com.fiap.challenge.cliente.domain.entities.Sessao;
import com.fiap.challenge.cliente.domain.port.ClienteRepository;
import com.fiap.challenge.cliente.domain.port.SessaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente Controller", description = "Operações relacionadas a clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final JwtService jwtService;
    private final SessaoRepository sessaoRepository;

    public ClienteController(
            ClienteRepository clienteRepository,
            JwtService jwtService,
            SessaoRepository sessaoRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.jwtService = jwtService;
        this.sessaoRepository = sessaoRepository;
    }

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getCpf().getValue(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getRole()
        );
    }

    @Operation(summary = "Cadastrar cliente (Público)")
    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrar(@RequestBody ClienteDTO dto) {
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new ClienteCpfJaCadastradoException("Já existe um cliente cadastrado com este CPF.");
        }
        Cliente cliente = new Cliente(dto.getCpf(), dto.getNome(), dto.getEmail(), "ROLE_CLIENTE");
        Cliente salvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }

    @Operation(summary = "Autenticar cliente (Público)")
    @PostMapping("/auth")
    public ResponseEntity<AuthResponseDTO> autenticar(@RequestBody AuthRequestDTO authRequest) {
        try {
            String cpf = authRequest.getCpf();
            String token;
            String roleParaToken;

            if (cpf == null || cpf.trim().isEmpty()) {
                roleParaToken = "ROLE_VISITANTE";
                token = jwtService.generateToken("visitante", List.of(roleParaToken));
            } else {
                Optional<Cliente> clienteOptional = clienteRepository.findByCpf(cpf);
                if (clienteOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                roleParaToken = clienteOptional.get().getRole();
                token = jwtService.generateToken(cpf, List.of(roleParaToken));
            }

            Sessao sessao = new Sessao();
            sessao.setCpf((cpf == null || cpf.trim().isEmpty()) ? "visitante" : cpf);
            sessao.setToken(token);
            sessao.setDataCriacao(new Date());
            sessaoRepository.save(sessao);

            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}