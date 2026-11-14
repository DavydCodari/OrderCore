# Package structure

Base package: `org.example.ordercore`.

Features: `product`, `user`, `cart`, `order`, `payment`, `inventory`,
`analytics`, and `auth`.

Each feature currently contains only these subpackages:

```text
dto/request/
dto/response/
mapper/
```

Future controllers, services, repositories, and domain classes can be placed
directly inside their feature package.

Security infrastructure remains under `security`: `config`, `authentication`,
`authorization`, `filter`, `token`, and `handler`.
Shared packages are `common.dto`, `common.exception`, and root `config`.

There are no package-info.java files or placeholder classes. Empty package
directories exist locally but Git will only track them once files are added.
