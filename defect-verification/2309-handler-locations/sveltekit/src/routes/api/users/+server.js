// users endpoint
import { json } from '@sveltejs/kit';

export async function GET() {
  return json([]);
}

export async function POST({ request }) {
  const body = await request.json();
  return json(body, { status: 201 });
}
