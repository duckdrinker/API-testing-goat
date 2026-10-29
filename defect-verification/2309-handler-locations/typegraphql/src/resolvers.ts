import { Resolver, Query, Mutation, Arg } from 'type-graphql';

@Resolver()
export class RecipeResolver {
  @Query(() => [String])
  recipes(): string[] {
    return [];
  }

  @Mutation(() => String)
  addRecipe(@Arg('title') title: string): string {
    return title;
  }
}
