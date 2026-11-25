package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import com.davyd.ordercore.cart.exception.ShoppingCartAlreadyExistsException;
import com.davyd.ordercore.cart.exception.ShoppingCartNotFoundException;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserService;
import com.davyd.ordercore.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;

    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               UserService userService,
                               ShoppingCartMapper shoppingCartMapper){
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.shoppingCartMapper =shoppingCartMapper;
    }

    public ShoppingCartResponse getShoppingCart(long currentUserId){
        return shoppingCartMapper.toResponse(
                shoppingCartRepository.findById(currentUserId).orElseThrow(() ->
                new ShoppingCartNotFoundException(currentUserId))
        );
    }

    @Transactional
    public ShoppingCartResponse createShoppingCart(long currentUserId){
        User user = userService.getUserEntity(currentUserId);

        ShoppingCart shoppingCart = new ShoppingCart(user);

        try {
            shoppingCartRepository.saveAndFlush(shoppingCart);
        } catch (DataIntegrityViolationException e){
            if (Validation.isConstraintViolation(e, "fk_shopping_carts_customer")){
                throw new UserNotFoundException(currentUserId);
            }

            if (Validation.isConstraintViolation(e, "pk_shopping_carts")){
                throw new ShoppingCartAlreadyExistsException(currentUserId);
            }

            throw e;
        }

        return shoppingCartMapper.toResponse(shoppingCart);
    }
}
