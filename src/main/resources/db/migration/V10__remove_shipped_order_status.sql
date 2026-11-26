-- Shipped orders remain in progress until their delivery is confirmed.
UPDATE orders SET status = 'PROCESSING' WHERE status = 'SHIPPED';
