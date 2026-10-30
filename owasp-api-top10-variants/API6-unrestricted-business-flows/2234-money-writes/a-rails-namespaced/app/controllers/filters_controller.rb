# Top-level controller with the same name: must NOT be chosen for /api/v1/filters
class FiltersController < ApplicationController
  def index
    render json: Filter.all
  end

  def update
    render json: { ok: true }
  end
end
