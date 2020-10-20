package br.com.zup.pact.provider.repository;

import br.com.zup.pact.provider.dto.AccountDetailsDTO;
import br.com.zup.pact.provider.dto.BalanceDTO;
import br.com.zup.pact.provider.entity.Account;
import br.com.zup.pact.provider.exception.ClientNotFoundException;
import br.com.zup.pact.provider.stub.AccountStub;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRepository {

    private final AccountStub accountStub;

    public Optional<AccountDetailsDTO> findByClientId(Integer clientId) {
        return Optional.ofNullable(Account.fromEntityToDto(accountStub.getAccounts().get(clientId)));
    }

    public Optional<List<AccountDetailsDTO>> getAll() {
        final List<Account> accounts = accountStub.getAccounts().values().stream()
                .collect(Collectors.toList());
        final List<AccountDetailsDTO> clientDetailsDTOS = new ArrayList<>();
        if (Objects.nonNull(accounts)) {
            for (Account account : accounts) {
                clientDetailsDTOS.add(Account.fromEntityToDto(account));
            }
        }
        return Optional.ofNullable(clientDetailsDTOS);
    }

    public Optional<BalanceDTO> getBalanceByClientId(Integer clientId) {
        final Account accountFound = accountStub.getAccounts()
                .values()
                .stream()
                .filter(account -> account.getClientId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new ClientNotFoundException("Account with id: " + clientId + " not found!"));
        return Optional.ofNullable(BalanceDTO.fromAccountToDTO(accountFound));
    }
}
