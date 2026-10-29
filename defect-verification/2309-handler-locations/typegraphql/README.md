# 2309 TypeGraphQL (handler location)

type-graphql 2.0.0-rc.2. 2 operations.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| Query recipes | src/resolvers.ts | 6-8 (5-8 if decorator included) | recipes (class RecipeResolver) | resolver method `recipes()` |
| Mutation addRecipe | src/resolvers.ts | 11-13 (10-13 if decorator included) | addRecipe (class RecipeResolver) | resolver method `addRecipe()` |
