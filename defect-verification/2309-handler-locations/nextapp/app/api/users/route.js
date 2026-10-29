// users route
import { NextResponse } from 'next/server';

export async function GET(request) {
  return NextResponse.json([]);
}

export async function POST(request) {
  const body = await request.json();
  return NextResponse.json(body, { status: 201 });
}
