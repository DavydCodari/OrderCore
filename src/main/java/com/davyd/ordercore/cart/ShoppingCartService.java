package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import com.davyd.ordercore.cart.dto.response.CartItemResponse;
import com.davyd.ordercore.cart.exception.ShoppingCartAlreadyExistsException;
import com.davyd.ordercore.cart.exception.ShoppingCartNotFoundException;
import com.davyd.ordercore.cart.exception.CartItemNotFoundException;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.product.Product;
import com.davyd.ordercore.product.ProductRepository;
import com.davyd.ordercore.product.exception.ProductNotFoundException;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserService;
import com.davyd.ordercore.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               UserService userService,
                               ShoppingCartMapper shoppingCartMapper,
                               CartItemRepository cartItemRepository,
                               ProductRepository productRepository){
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.shoppingCartMapper =shoppingCartMapper;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartItemResponse addCartItem(long currentUserId, long productId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findById(currentUserId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(currentUserId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        CartItem item = cartItemRepository.save(new CartItem(product, cart, quantity));
        return shoppingCartMapper.toItemResponse(item);
    }

    @Transactional
    public void deleteCartItem(long currentUserId, long itemId) {
        if (cartItemRepository.deleteItemFromCart(itemId, currentUserId) == 0) {
            throw new CartItemNotFoundException(itemId);
        }
    }

    @Transactional
    public void clearCart(long currentUserId) {
        if (!shoppingCartRepository.existsById(currentUserId)) {
            throw new ShoppingCartNotFoundException(currentUserId);
        }
        cartItemRepository.deleteItemsFromCart(currentUserId);
    }

    public ShoppingCartResponse getShoppingCart(long currentUserId){
        ShoppingCart cart = shoppingCartRepository.findById(currentUserId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(currentUserId));
        return shoppingCartMapper.toResponse(cart,
                cartItemRepository.findByShoppingCart_ShoppingCartIdOrderByIdAsc(currentUserId));
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

        return shoppingCartMapper.toResponse(shoppingCart, List.of());
    }
}
