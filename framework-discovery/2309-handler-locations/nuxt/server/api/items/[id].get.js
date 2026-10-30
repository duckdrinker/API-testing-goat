// item by id
import { defineEventHandler } from 'h3';

export default defineEventHandler((event) => {
  const id = event.context.params.id;
  return { id };
});
