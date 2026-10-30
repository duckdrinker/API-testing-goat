// users resource route
import { json } from '@remix-run/node';

export async function loader() {
  return json([]);
}

export async function action({ request }) {
  const body = await request.json();
  return json(body, { status: 201 });
}
